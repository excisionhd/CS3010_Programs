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
        if fmid == 0:
            return mid
        if fx1 * fmid < 0:
            x2 = mid  
        else:
            x1 = mid
    return

def Newton(polynomial, max_iter, p1):
    x1 = float(p1)
    fx1 = polynomial.evaluate(x1)

    for i in range(max_iter):
        fd = polynomial.evalDeriv(x1)

        if (fd == 0):
            print("Minimum/maximum reached: ({} , {})".format(x1,fx1))
            return x1
        d = fx1 / fd
        x1 = x1 - d
        fx1 = polynomial.evaluate(x1)

        if (d == 0):
            print("Converged after {} iterations...".format(i+1))
            return x1

    return

def Secant(polynomial, max_iter, p1, p2):
    x1 = p1
    x2 = p2

    fx1 = polynomial.evaluate(p1)
    fx2 = polynomial.evaluate(p2)
    """
    fa := call f(a)
  fb := call f(b)

  if |fa| > |fb| then
    swap(a, b)
    swap(fa, fb)
  end if

  for it <- 1 to maxIter
    if |fa| > |fb| then
      swap(a, b)
      swap(fa, fb)
    end if

    d := (b - a) / (fb - fa)
    b := a
    fb := fa
    d := d * fa

    if |d| < eps then
      print "Algorithm has converged after #{it} iterations!"
      return a
    end if

    a := a - d
    fa := call f(a)
  end for

    print "Maximum number of iterations reached!"
    return a
    """



def main():
    poly = ImportPolynomial("sys1.pol")
    print("X = {}".format(Newton(poly, 10000, -1)))

main()