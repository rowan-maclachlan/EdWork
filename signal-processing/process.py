#!/usr/local/bin/python3
import argparse

import os
import sys
from typing import *

import numpy as np
import matplotlib as mpl
import matplotlib.pyplot as plt
from scipy import signal
from scipy import fft
from pandas import read_csv
# for sm.nonparametric.lowess smoothing
import statsmodels.api as sm
from matplotlib.legend import _get_legend_handles_labels


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

uncalibrated_files = args.UncalibratedFiles

def filter_data(data: List[List[float]]) -> List[List[float]]:
    """
    Remove outliers using medfilt
    https://docs.scipy.org/doc/scipy/reference/generated/scipy.signal.medfilt.html
    """
    filter_size = 9 # must be odd - size of filter
    x, y = zip(*data)
    return list(zip(x, signal.medfilt(y, filter_size)))

def smooth_data(data: List[List[float]]) -> List[List[float]]:
    """
    Smooth data
    https://www.statsmodels.org/stable/generated/statsmodels.nonparametric.smoothers_lowess.lowess.html
    """
    x, y = zip(*data)
    frac = 0.1 # low for fine tuning high for messy data
    return sm.nonparametric.lowess(y, x, frac)

def build_graph(ax, title):
    ax.set_xlabel("time (s)")
    ax.set_ylabel("signal measurement")
    ax.set_title(title)
    ax.set_xlim(0, 1)
    ax.set_title(title)
    return ax

def build_graphs():
    x_label = "time (s)"
    y_label = "signal measurement"
    x_lim = (0, 1)
    fig = plt.figure()
    fig.set_size_inches(14, 10)
    subplots = 3 # raw, filtered, filtered and smoothed
    gs = fig.add_gridspec(subplots, left=0.2, hspace=0.5)
    axs = gs.subplots(sharex=True)
    raw_axs = build_graph(axs[0], "Raw Data")
    filtered_axs = build_graph(axs[1], "Filtered Data")
    smoothed_axs = build_graph(axs[2], "Smoothed Data")
    return fig

def plot_graphs(raw_axs, filtered_axs, smoothed_axs, cald, filtd, smoothd, lbl):
    x, y = zip(*cald)
    raw_axs.scatter(x, y, s=1, label=lbl)
    x, y = zip(*filtd)
    filtered_axs.scatter(x, y, s=1, label=lbl)
    x, y = zip(*smoothd)
    smoothed_axs.plot(x, y, label=lbl)

cal_data = read_csv(calibrated_file).values
filtered_data = filter_data(cal_data)
smoothed_data = smooth_data(filtered_data)

unc_data = [ read_csv(file).values for file in uncalibrated_files ]
filtered_unc_data = [ filter_data(uncd) for uncd in unc_data ]
smoothed_unc_data = [ smooth_data(funcd) for funcd in filtered_unc_data ]

fig = build_graphs()
raw_axs, filtered_axs, smoothed_axs = fig.axes

plot_graphs(raw_axs, filtered_axs, smoothed_axs, cal_data, filtered_data, smoothed_data, calibrated_file)

for (uncd, funcd, suncd, file) in zip(unc_data, filtered_unc_data, smoothed_unc_data, uncalibrated_files):
    plot_graphs(raw_axs, filtered_axs, smoothed_axs, uncd, funcd, suncd, file)

handles, labels = fig.axes[0].get_legend_handles_labels()
fig.legend(handles, labels, loc='center left')
plt.show()

sys.exit(0)

