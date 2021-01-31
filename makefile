# Based on https://stackoverflow.com/q/25735553 by WizDom
#   and https://marketplace.visualstudio.com/items?itemName=adriano-markovic.c-cpp-makefile-project by Adriano Markovic
#   (both with modifications)

################################################################################
# INSTRUCTIONS
#   1) This makefile MUST be in the same directory as your .java files
#   2) Customize the section below for your project
#   3) You'll need to create the CLASSDIR yourself
################################################################################
# Directory structure of your project
## Location of the .class files
CLASSDIR = ./classes
## Location of the .jar   file
JARDIR = .

# Names specific to your project
## The class with `public static void main(String[] args)`
MAINCLASS = CLI
## Used to craft the name for the .jar file
APPNAME = CoulAdj

################################################################################
# Nothing to customize until next section
################################################################################
# This makefile won't work if SRCDIR isn't `.` and I don't understand why :(
SRCDIR = .
JARNAME = $(APPNAME).jar
JARFILE = $(JARDIR)/$(JARNAME)

sources=$(wildcard $(SRCDIR)/*.java)
classes=$(sources:$(SRCDIR)/%.java=$(CLASSDIR)/%.class)

all: $(JARFILE) test

$(JARFILE): $(classes)
	jar cef $(MAINCLASS) $(JARFILE) -C $(CLASSDIR) .

$(CLASSDIR)/%.class: $(SRCDIR)/%.java
	javac -d $(CLASSDIR) $<

.PHONY: only clean fresh
only: $(JARFILE)

clean:
	rm -f $(JARFILE)
	rm -f $(CLASSDIR)/*.class

fresh: clean all

################################################################################
# Customize your tests below
################################################################################
.PHONY: test test-simple test-corr test-perf

test: test-corr

test-simple: 
	java -jar $(JARFILE) ./tests/samples/sample-size-1.png ./tests/results/result-size-1.tsv

test-corr: 
	bash test-corr.sh

test-perf: 
	bash test-perf.sh
