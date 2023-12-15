from .henk import main

from pathlib import Path

parent = Path(__file__).parent

def part_one(testfile = False):
    path = parent / ("input.txt" if not testfile else "test.txt")
    print(main(path, 1))

def part_two(testfile = False):
    path = parent / ("input.txt" if not testfile else "test2.txt")
    print(main(path, 2))
