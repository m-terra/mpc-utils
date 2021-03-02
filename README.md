# mpc-utils for AKAI MPC One/X/Live/Live2

## Non-Destructive

mpc-utils will not alter the original projects but create a new project version in the specified target directory.

### Sequence Reordering

Reorders the sequences according to a song. Default song is '1'. Sequences that are not used in the specified song will
be appended after the sequences used in the song.

### Project Filtering

Filters out projects that contain a sequence with the specified name. Default name is 'Live'. Creates a text file with
the BPM of all filtered projects.

## Setup

- A Java Runtime Environment (JRE) 11 or greater is installed
- Download the latest binary jar from https://github.com/andybaer/mpc-utils/releases

## Usage

    java -jar <mpc-utils-jar> <command> <scanDirectory> <targetDirectory> [songNumber|sequenceName]

    <mpc-utils-jar> The binary of this software
    <command> [reorder|filter|reorderAndLiveSet]
    <scanDirectory> The directory containing the MPC projects to reorder
    <targetDirectory> The directory to save the reordered projects
    [songNumber|sequenceName] Optional. reorder: song number, default '1', filter: sequence name, default 'Live'