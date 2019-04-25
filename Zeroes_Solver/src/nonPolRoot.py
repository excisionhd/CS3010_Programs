import math

def func1(x):
    return (x ** 3) - (2 * math.sin(x))

def func2(x):
    return  x + 10 - (x * math.cosh(50/x))


def Bisection(max_iter, p1, p2):
    x1 = float(p1)
    x2 = float(p2)

    fx1 = func1(x1)
    fx2 = func1(x2)

    print("g({}) = {}, g({}) = {}".format(x1, fx1, x2, fx2))

    if(fx1 * fx2 > 0):
        print("Invalid points, no roots detected between.")
        return

    for i in range(max_iter):
        mid = (x1 + x2)/2
        fmid = func1(mid)
        print("Iter {}: XA = {}, XB = {}, XMid: {}".format(i+1, x1, x2, mid))
        if fmid == 0:
            print("Converged after {} iterations...".format(i+1))
            return mid
        if fx1 * fmid < 0:
            print("g(XMid) = g({}) = {}.  {} > 0 -> XB = {}".format(mid, fmid, fmid, mid))
            x2 = mid  
        else:
            print("g(XMid) = g({}) = {}.  {} < 0 -> XA = {}".format(mid, fmid, fmid, mid))
            x1 = mid

    print("Failed to converge after {} iterations".format(max_iter))
    return

def main():
    Bisection(50, 0.5, 2)

main()