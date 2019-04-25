import sys
import os
import argparse

"""
Zeroes.py
Purpose: Calculate the zeroes of a given polynomial (via input file) using
1. Bisection
2. Newton
3. Secant
4. Hybrid (Bisection + Newton)
"""

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

class Result():
    def __init__(self, root, iterations, outcome):
        self.root = root
        self.iterations = iterations
        self.outcome = outcome

    def __str__(self):
        return " ".join([str(self.root), str(self.iterations), str(self.outcome)])

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

    fx1 = polynomial.evaluate(x1)
    fx2 = polynomial.evaluate(x2)
    print("f({}) = {}, f({}) = {}".format(x1, fx1, x2, fx2))

    if(fx1 * fx2 >= 0):
        print("Invalid points, no roots detected between.")
        return Result((x1, fx1), 0, "Failed")

    for i in range(max_iter):
        mid = (x1 + x2)/2
        fmid = polynomial.evaluate(mid)
        print("Iter {}: XA = {}, XB = {}, XMid: {}".format(i+1, x1, x2, mid))
        if fmid == 0:
            print("Converged after {} iterations...".format(i+1))
            return Result((mid, fmid), i+1, "Converged")
        if fx1 * fmid < 0:
            print("f(XMid) = g({}) = {}.  {} > 0 -> XB = {}".format(mid, fmid, fmid, mid))
            x2 = mid  
        else:
            print("f(XMid) = g({}) = {}.  {} < 0 -> XA = {}".format(mid, fmid, fmid, mid))
            x1 = mid

    print("Failed to converge after {} iterations".format(max_iter))
    return Result((mid, fmid), max_iter, "Failed")

def Newton(polynomial, max_iter, p1):
    x1 = float(p1)
    fx1 = polynomial.evaluate(x1)

    print("X0 = {}, f(X0) = {}".format(x1, fx1))

    for i in range(1, max_iter):
        fd = polynomial.evalDeriv(x1)

        if (fd == 0):
            print("Minimum/maximum reached: ({} , {})".format(x1,fx1))
            return Result((x1, fx1), i, "Min/max_stopped")
        _fx1 = fx1
        _x1 = x1
        _fd = fd

        d = fx1 / fd
        x1 = x1 - d
        fx1 = polynomial.evaluate(x1)

        print("X{} = X{} - f(X{})/f'(X{}) = {} - {}/{} = {}".format(i,i-1,i-1,i-1,_x1,_fx1,_fd, x1))

        if (d == 0):
            print("Converged after {} iterations...".format(i+1))
            return Result((x1, fx1), i+1, "Converged")

    print("Failed to converge after {} iterations".format(max_iter))
    return Result((x1, fx1), max_iter, "Failed")

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

    fx1 = polynomial.evaluate(x1)
    fx2 = polynomial.evaluate(x2)

    if (abs(fx1) > abs(fx2)):
        Swap(x1,x2,fx1,fx2)

    print("X0 = {}, X1 = {}".format(x1, x2))
    
    for i in range(2, max_iter):
        if (abs(fx1) > abs(fx2)):
            Swap(x1,x2,fx1,fx2)

        _x1 = x1
        _x2 = x2
        _fx1 = fx1
        _fx2 = fx2

        d = (x2 - x1) / (fx2 - fx1)
        x2 = x1
        fx2 = fx1
        d = d * fx1

        if (d == 0):
            print("Converged after {} iterations".format(i-1))
            return Result((x1, fx1), i-1, "Converged")
            
        x1 = x1 - d
        fx1 = polynomial.evaluate(x1)

        print("X{} = X{} - ( (X{} - X{}) / (f(X{}) - f(X{}) ) * f(X{}) = {} - ( ({} - {}) / ({} - {}) ) * {} = {}".format(i, i-1, i-1 ,i-2, i-1, i-2, i-1, _x1, _x1, _x2, _fx2, _fx1,_fx2, x1))

    print("Failed to convergence after {} iterations".format(max_iter))
    return Result((x1, fx1), max_iter, "Failed")


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
        res = Newton(poly, args.maxIter, args.initP)
        print("Newton")
        print("({} , {})".format(*res.root))
    elif (args.sec):
        res = Secant(poly, args.maxIter, args.initP, args.initP2)
        print("Secant")
        print("({} , {})".format(*res.root))
    else:
        res = Bisection(poly, args.maxIter, args.initP, args.initP2)
        print("Bisection")
        print("({} , {})".format(*res.root))

    with open(args.polyFileName.split(".")[0] + ".sol", 'w') as file:
        file.write(str(res))
    



main()