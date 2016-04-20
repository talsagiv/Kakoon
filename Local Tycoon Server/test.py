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


a = GoogPlac(32.095119,34.815811,500,"*","AIzaSyAGD1qz-6R_dBB4fjOV6LN_3chGHlLwP2c")
message =""
for place in a["results"]:
    x = IterJson(place)
    business = Business.Business(x[0], 5000, None, str(x[2])+","+str(x[3]), "20.4.2016", None, 1) #TODO make dynamic
    bjc = business.To_String()
    message += bjc +"|"
message = message[:-1]
print message



#print a