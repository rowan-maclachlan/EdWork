#!/usr/local/bin/python3
import argparse

import os
import sys

my_parser = argparse.ArgumentParser(description='Calibrate sensor readings')

# Add the arguments
my_parser.add_argument('CalibratedFile', \
                       metavar='calibrated_file',\
                       type=str, \
                       help='the path to the calibrated file reading')

my_parser.add_argument('UncalibratedFiles', \
                        metavar='uncalibrated_files', \
                        type=str, \
                        nargs='+', \
                        help='A list of paths to uncalibrated files - at least one')

# Execute the parse_args() method
args = my_parser.parse_args()

calibrated_file = args.CalibratedFile

if not os.path.isfile(calibrated_file):
    print(f'The calibrated file path {calibrated_file} specified does not exist')
    sys.exit(1)

for uncalibrated_file in args.UncalibratedFiles:
    if not os.path.isfile(uncalibrated_file):
        print(f'The uncalibrated file path {uncalibrated_file} specified does not exist')
        sys.exit(1)

sys.exit(0)

