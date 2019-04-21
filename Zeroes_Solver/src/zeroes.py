import sys
import os
import argparse

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

def Bisection(polynomial, max_iter, p1, p2):
    fx1 = polynomial.evaluate(p1)
    fx2 = polynomial.evaluate(p2)

    if(fx1 * fx2 > 0):
        return

    x1 = p1
    x2 = p2

    for i in range(max_iter):
        mid = float((x1 + x2)/2)
        fmid = polynomial.evaluate(mid)
        print(fmid)
        if fmid == 0:
            return mid
        if fx1 * fmid < 0:
            x2 = mid  
        else:
            x1 = mid

    return



def main():
    poly = ImportPolynomial("sys1.pol")
    print(Bisection(poly, 100, -5, 5))

main()