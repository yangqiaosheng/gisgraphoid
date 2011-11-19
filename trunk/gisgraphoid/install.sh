#!/bin/bash

mvn clean install
cp target/*.jar ~/.m2/repository/com/gisgraphy/gisgraphoid/1.0/
