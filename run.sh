#!/usr/bin/env bash
gnome-terminal -e "docker run --rm -it -e SYSTEM_TYPE=server fibbing"
gnome-terminal -e "docker run --rm -it -e SYSTEM_TYPE=Client fibbing"

