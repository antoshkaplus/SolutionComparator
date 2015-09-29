import sys
import os
import random

# first argument path to solutions
# if not provided use "sol_scores"
path = "../sol_scores"
if len(sys.argv) != 1:
    path = sys.argv[1]

if not os.path.exists(path):
    os.makedirs(path)

n_solutions = 10
n_samples = 100
for i in range(n_solutions):
    filename = "solution_%d.txt" % i
    with open(os.path.join(path, filename) , "w") as f:
        for k in range(n_samples):
            f.write("%d:%f\n" % (k, random.random()))
            