import sys
import os

class Polynomial():
    def __init__(self, degree, coeff):
        self.degree = degree
        self.coeff = coeff
        self.derivative = [co * (len(coeff[:-1]) -n) for n, co in enumerate(coeff[:-1])]

    def evaluate(self, x):
        result = 0
        for i, num in enumerate(self.coeff):
            result += num * (x ** (self.degree - i))
        return result

    def evalDeriv(self, x):
        result = 0
        for i, num in enumerate(self.derivative):
            result += num * (x ** (len(self.derivative) -1 - i))
        return result


def ImportPolynomial(path):
    if not os.path.isfile(path):
        print("File path {} does not exist. Exiting...".format(path))
        sys.exit()

    with open(path, 'r') as file:
        degree = int(file.readline())
        coeff = [int(i) for i in file.readline().split(' ')]

    return Polynomial(degree, coeff)
