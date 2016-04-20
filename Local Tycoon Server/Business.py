import json


class Business:
    def __init__(self, name, price, owner,  coords, purchase_date, improvements, payoff):
        self.name = name
        self.owner = owner
        self.coords = coords
        self.purchase_date = purchase_date
        self.improvments = improvements
        self.payoff = payoff
        self.price = price

    '''
    def calculate_price(self):
        return 10 #TODO VERY IMPORTANT MUCHO IMPORTANT ACTUALLY MAKE THIS FUNCTION!!!!'''

    #calculates the radius around the business in which you can purchase other businesses
    def calculate_radius(self):
        pass #TODO THIS FUNCTION

    #converts object to json string
    def To_String(self):
        return json.dumps(self, default=lambda o: o.__dict__)

