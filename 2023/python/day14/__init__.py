from .henk import main

from pathlib import Path

path = Path(__file__).parent / "input.txt"

def part_one():
    print(main(path, 1))

def part_two():
    print(main(path, 2))
