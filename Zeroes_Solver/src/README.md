## Instructions
usage: `polRoot.py [-h] [-newt | -sec] [-maxIter MAXITER] initP [initP2] polyFileName`

Evaluate zeroes of polynomials imported via file.  

positional arguments:  
  initP             specify initial point  
  initP2            specify initial point 2  
  polyFileName      specify the polynomial file  

optional arguments:  
  -h, --help        show this help message and exit  
  -newt             run newton method for zeroes  
  -sec              run secant method for zeroes  
  -maxIter MAXITER  specify max iterations
