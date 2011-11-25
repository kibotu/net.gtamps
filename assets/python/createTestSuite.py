#! /usr/bin/python

import sys
import os

target_dir = "/home/til/beuth/mp2/projekt/shared/test"
suite_file_name = "TestAllTest.java"
suite_base_package = "net.gtamps.shared"
suite_class_name = "TestAllTest"

suite_header="""
package net.gtamps.shared;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
"""

suite_footer = """
})
public class %s {
}
""" % suite_class_name


def collectTestClasses(dir):
	pass
	