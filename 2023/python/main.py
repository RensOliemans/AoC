#!/usr/bin/env python3
import argparse
import importlib
import sys
from datetime import datetime

parser = argparse.ArgumentParser(prog='Advent of Code 2023')

today = datetime.today().day
parser.add_argument('dagen', default=today, type=int,
                    choices=range(1, 26),
                    nargs='*',
                    help=f'Dag, defaults naar vandaag ({today})')
parser.add_argument('-t', '--test',
                    help='Use testfile or not',
                    action='store_true')

args = parser.parse_args()

dagen = args.dagen if type(args.dagen) is list else [args.dagen]

success = False
if type(dagen) is list:
    for dag in dagen:
        try:
            module = importlib.import_module(f"day{dag}")
        except ModuleNotFoundError:
            print(f"Dag {dag} bestaat niet!", file=sys.stderr)
            continue

        print(f"Dag {dag}:")
        module.part_one(args.test)
        module.part_two(args.test)
        success = True

if not success:
    sys.exit(1)

