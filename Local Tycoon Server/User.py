import json
from collections import namedtuple

class User:
    def __init__(self, username, money, businesses):
        self.username = username
        self.money = money
        self.businesses = businesses
        self.recalculate_networth()
        self.around_me = []
        self.around_look = []

    '''def __init__(self, object):
        x = json.loads(object, object_hook=lambda d: namedtuple('X', d.keys())(*d.values()))
        self.username = x.username
        self.money = x.money
        self.businesses = x.businesses
        self.recalculate_networth()
        self.around_me = []'''

    #adds a business to the businesses list
    def add_business(self, business):
        self.businesses.append(business)
        self.recalculate_networth()

    #adds money to the user
    def change_money(self, money):
        self.money += money
        self.recalculate_networth()

    #removes a business from the businesses list
    def remove_business(self, business):
        self.businesses.remove(business)
        self.recalculate_networth()

    #changes the network based on businesses and money
    def recalculate_networth(self):
        self.networth = self.money
        for business in self.businesses:
            self.networth += business.get_price() #TODO remember to add get price

    #converts object to json string
    def To_String(self):
        return json.dumps(self, default=lambda o: o.__dict__)


