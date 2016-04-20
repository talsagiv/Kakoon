import socket
import select
import sqlite3
import hashlib
import base64
import User
import json
from collections import namedtuple
import unicodedata
import Business

class Server:
    def __init__(self, port):
        self.server_socket = socket.socket()
        self.server_socket.bind(("0.0.0.0", port))
        self.server_socket.listen(10)
        self.messages_to_send = []
        self.open_client_sockets = []
        self.conn = sqlite3.connect("database.db")
        self.conn.execute('''CREATE TABLE IF NOT EXISTS BUSINESS
                           ( ID TEXT PRIMARY KEY   ,
                           OBJECT           TEXT    NOT NULL);''')


        self.conn.execute('''CREATE TABLE IF NOT EXISTS USER
                           ( ID INTEGER PRIMARY KEY   AUTOINCREMENT,
                           USERNAME         TEXT   NOT NULL,
                           PASS             TEXT   NOT NULL,
                           OBJECT           TEXT    NOT NULL);''')
        self.online_clients = []
        self.func_dic= {
            "reg": self.register,
            "lgn": self.login,
            "loc": self.around_user,
            "buy": self.buy_business,
            "loo": self.look_around,

        }


    #send messages
    def send_waiting_messages(self, wlist):
        for message in self.messages_to_send:
            (client_socket, data) = message
            if client_socket in wlist:
                #length = str(len(data)).zfill(5)
                print len(data)
                client_socket.sendall(data)
                self.messages_to_send.remove(message)


    def prot_to_list(self, data):
        lst = []
        command = data[:3]
        data = data[4:]
        lst.append(command)
        temp_lst = data.split("|")
        for i in temp_lst:
            lst.append(base64.b64decode(i))
        return lst


    def register(self, lst, soc):
        cur = self.conn.cursor()
        cur.execute("SELECT * FROM USER WHERE USERNAME='" + lst[1] + "'")
        if cur.fetchone() is None:
            user = User.User(lst[1], 10000, [])
            cur.execute("INSERT INTO USER (USERNAME, PASS, OBJECT) VALUES ('"+lst[1]+"', '"+lst[2]+"', '"+user.To_String() + "')")
            self.conn.commit()
            self.online_clients.append((user, soc))
            self.messages_to_send.append((soc, "SUCCESS"))

        else:
            self.messages_to_send.append((soc, "FAILURE"))



    def login(self, lst, soc):
        cur = self.conn.cursor()
        cur.execute("SELECT PASS FROM USER WHERE USERNAME='" + lst[1] + "'")
        data = cur.fetchone()
        if data is None:
            self.messages_to_send.append((soc, "FAILURE"))
        else:
            if unicodedata.normalize('NFKD', data[0]).encode('ascii','ignore') == lst[2]:
                cur = self.conn.cursor()
                cur.execute("SELECT OBJECT FROM USER WHERE USERNAME='" + lst[1] + "'")
                object = cur.fetchone()
                x = json.loads(unicodedata.normalize('NFKD', object[0]).encode('ascii','ignore'), object_hook=lambda d: namedtuple('X', d.keys())(*d.values()))
                user = User.User(x.username, x.money, x.businesses)
                self.online_clients.append((user, soc))
                self.messages_to_send.append((soc, "SUCCESS"))
            else:
                self.messages_to_send.append((soc, "FAILURE"))

    # Buys Business For The User After Checks returns false if not in range or not enough money
    def buy_business(self, lst, soc):
        for client in self.online_clients:
            if client[1] == soc:
                user = client[0]

        for business in user.around_me:
            if lst[1] != business.name:
                return False

        cur = self.conn.cursor()
        cur.execute("SELECT OBJECT FROM BUSINESS WHERE ID='" + str(lst[1])+","+str(lst[2]) + "'")
        data = cur.fetchone()
        x = json.loads(unicodedata.normalize('NFKD', data[0]).encode('ascii','ignore'), object_hook=lambda d: namedtuple('X', d.keys())(*d.values()))
        if user.money < x.price:
            pass#TODO DO SOMETHING HERE
        else:
            user.money -= x.price

            business = Business.Business(x.name, x.price, x.owner, x.coords, x.purchase_date, x.improvments, x.payoff)
            user.businesses.append(business)

            cur = self.conn.cursor()
            cur.execute("INSERT INTO BUSINESS (ID, OBJECT) VALUES ('" + str(lst[1])+","+str(lst[2]) + "', '" + business.To_String() + "' ")



    def around_user(self, lst, soc):
        print 1
        for client in self.online_clients:
            if client[1] == soc:
                user = client[0]
        user.around_me = []
        import unicodedata
        import urllib, json
        import json
        from collections import namedtuple
        import Business

        #Grabbing and parsing the JSON data
        def GoogPlac(lat,lng,radius,types,key):
            #making the url
            AUTH_KEY = key
            LOCATION = str(lat) + "," + str(lng)
            RADIUS = radius
            TYPES = types
            MyUrl = ('https://maps.googleapis.com/maps/api/place/nearbysearch/json'
                   '?location=%s'
                   '&radius=%s'
                   '&types=%s'
                   '&sensor=false&key=%s') % (LOCATION, RADIUS, TYPES, AUTH_KEY)
            #grabbing the JSON result
            response = urllib.urlopen(MyUrl)
            jsonRaw = response.read()
            jsonData = json.loads(jsonRaw)
            return jsonData

        #This is a helper to grab the Json data that I want in a list
        def IterJson(place):
            x = [place['name'], place['reference'], place['geometry']['location']['lat'],
            place['geometry']['location']['lng'], place['vicinity']]
            return x


        a = GoogPlac(lst[1],lst[2],500,"*","AIzaSyAGD1qz-6R_dBB4fjOV6LN_3chGHlLwP2c")
        print a
        message = ""
        for place in a["results"]:
            x = IterJson(place)
            cur = self.conn.cursor()
            cur.execute("SELECT OBJECT FROM BUSINESS WHERE ID='" + str(lst[1])+","+str(lst[2]) + "'")
            data = cur.fetchone()
            print data
            if data is None:
                business = Business.Business(x[0], 5000, None, str(x[2])+","+str(x[3]), "20.4.2016", None, 1)  # TODO make dynamic
                user.around_me.append(business)
                bjc = business.To_String()
                message += bjc + "|"
                print message

            else:
                message += data[0] + "|"  # TODO check if unicode decode/encode needed


        message = message[:-1]
        print message
        length = str(len(message)).zfill(5)
        self.messages_to_send.append((soc, length + message))

    #Checks in API What Bussnises are Around Where he Looks and sends to User
    def look_around(self, lst, soc):
        for client in self.online_clients:
            if client[1] == soc:
                user = client[0]
        user.around_look = []
        import unicodedata
        import urllib, json
        import json
        from collections import namedtuple
        import Business

        #Grabbing and parsing the JSON data
        def GoogPlac(lat,lng,radius,types,key):
            #making the url
            AUTH_KEY = key
            LOCATION = str(lat) + "," + str(lng)
            RADIUS = radius
            TYPES = types
            MyUrl = ('https://maps.googleapis.com/maps/api/place/nearbysearch/json'
                   '?location=%s'
                   '&radius=%s'
                   '&types=%s'
                   '&sensor=false&key=%s') % (LOCATION, RADIUS, TYPES, AUTH_KEY)
            #grabbing the JSON result
            response = urllib.urlopen(MyUrl)
            jsonRaw = response.read()
            jsonData = json.loads(jsonRaw)
            return jsonData

        #This is a helper to grab the Json data that I want in a list
        def IterJson(place):
            x = [place['name'], place['reference'], place['geometry']['location']['lat'],
            place['geometry']['location']['lng'], place['vicinity']]
            return x


        a = GoogPlac(lst[1],lst[2],500,"*","AIzaSyAGD1qz-6R_dBB4fjOV6LN_3chGHlLwP2c")
        message = ""
        for place in a["results"]:
            x = IterJson(place)
            cur = self.conn.cursor()
            cur.execute("SELECT OBJECT FROM BUSINESS WHERE ID='" + x[4] + "'")
            data = cur.fetchone()
            if data is None:
                business = Business.Business(x[0], 5000, None, str(x[2])+","+str(x[3]), "20.4.2016", None, 1) #TODO make dynamic
                user.around_look.append(business)
                bjc = business.To_String()
                message += bjc +"|"
            else:
                message += data[0] + "|"  # TODO check if unicode decode/encode needed

        message = message[:-1]
        length = str(len(message)).zfill(5)
        self.messages_to_send.append((soc, length + message))



    def run_select(self):

        while True:

            rlist, wlist, xlist = select.select( [self.server_socket] + self.open_client_sockets, self.open_client_sockets, [] )

            for current_socket in rlist:

                #New Client
                if current_socket is self.server_socket:
                    (new_socket, address) = self.server_socket.accept()
                    self.open_client_sockets.append(new_socket)

                #New data for existing client
                else:

                    #recieve data
                    data = current_socket.recv(1024)

                    #terminate connection
                    if data == "":
                        self.open_client_sockets.remove(current_socket)

                    #handle data
                    else:
                        lst = self.prot_to_list(data)
                        self.func_dic[lst[0]](lst, current_socket)





            #send messages
            self.send_waiting_messages(wlist)


def main():
    my_server = Server(50000)
    my_server.run_select()



if __name__ == "__main__":
    main()