import sys
import os
import argparse

class Polynomial():
    def __init__(self, degree, coeff):
        self.degree = degree
        self.coeff = coeff
        self.derivative = [co * (len(coeff[:-1]) -n) for n, co in enumerate(coeff[:-1])]

    def evaluate(self, x):
        result = 0.0
        for i, num in enumerate(self.coeff):
            result += num * (x ** (self.degree - i))
        return result

    def evalDeriv(self, x):
        result = 0.0
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
    x1 = float(p1)
    x2 = float(p2)

    fx1 = polynomial.evaluate(p1)
    fx2 = polynomial.evaluate(p2)

    if(fx1 * fx2 > 0):
        print("Invalid points, must be opposite signs.")
        return (x1, fx1)

    for i in range(max_iter):
        mid = (x1 + x2)/2
        fmid = polynomial.evaluate(mid)
        if fmid == 0:
            print("Converged after {} iterations...".format(i+1))
            return (mid, fmid)
        if fx1 * fmid < 0:
            x2 = mid  
        else:
            x1 = mid

    print("Failed to converge after {} iterations".format(max_iter))
    return (mid, fmid)

def Newton(polynomial, max_iter, p1):
    x1 = float(p1)
    fx1 = polynomial.evaluate(x1)

    for i in range(max_iter):
        fd = polynomial.evalDeriv(x1)

        if (fd == 0):
            print("Minimum/maximum reached: ({} , {})".format(x1,fx1))
            return (x1, fx1)
        d = fx1 / fd
        x1 = x1 - d
        fx1 = polynomial.evaluate(x1)

        if (d == 0):
            print("Converged after {} iterations...".format(i+1))
            return (x1, fx1)

    return (x1, fx1)

def Swap(x1, x2, fx1, fx2):
    hold = x1
    x1 = x2
    x2 = hold

    holdf = fx1
    fx1 = fx2
    fx2 = holdf

def Secant(polynomial, max_iter, p1, p2):
    x1 = float(p1)
    x2 = float(p2)

    fx1 = polynomial.evaluate(p1)
    fx2 = polynomial.evaluate(p2)

    if (abs(fx1) > abs(fx2)):
        Swap(x1,x2,fx1,fx2)
    
    for i in range(max_iter):
        if (abs(fx1) > abs(fx2)):
            Swap(x1,x2,fx1,fx2)

        d = (x2 - x1) / (fx2 - fx1)
        x2 = x1
        fx2 = fx1
        d = d * fx1

        if (d == 0):
            print("Converged after {} iterations".format(i+1))
            return (x1, fx1)

        x1 = x1 - d
        fx1 = polynomial.evaluate(x1)

    print("No convergence after {} iterations".format(max_iter))
    return (x1, fx1)


def main():
    parser = argparse.ArgumentParser(description="Evaluate zeroes of polynomials imported via file.")
    mutual_args = parser.add_mutually_exclusive_group()
    mutual_args.add_argument("-newt", help="run newton method for zeroes", action="store_true")
    mutual_args.add_argument("-sec", help="run secant method for zeroes", action="store_true")
    parser.add_argument("-maxIter", help="specify max iterations", type=int, default=10000)
    parser.add_argument("initP", help="specify initial point", type=float)
    parser.add_argument("initP2", help="specify initial point 2", nargs='?', type=float)
    parser.add_argument("polyFileName", help="specify the polynomial file")
    args = parser.parse_args()

    if (not args.newt and args.initP2 is None):
        parser.error("Algorithm requires two points.")
    poly = ImportPolynomial(args.polyFileName)

    if (args.newt):
        print("Newton")
        print("({} , {})".format(*Newton(poly, args.maxIter, args.initP)))
    elif (args.sec):
        print("Secant")
        print("({} , {})".format(*Secant(poly, args.maxIter, args.initP, args.initP2)))
    else:
        print("Bisection")
        print("({} , {})".format(*Bisection(poly, args.maxIter, args.initP, args.initP2)))

    



main()