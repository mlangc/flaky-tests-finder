#!/bin/bash

SCALA_IDE_HOME="/home/mlangc/Development/kit/scala-ide"


cd "$SCALA_IDE_HOME"
./build-all.sh "-Pscala-2.11.x -Peclipse-mars verify"
