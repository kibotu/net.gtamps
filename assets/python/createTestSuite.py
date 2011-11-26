#! /usr/bin/python

import sys
import os
import datetime

suite_base_dir = "/home/til/beuth/mp2/projekt/shared/test/net/gtamps/shared"
suite_base_package = "net.gtamps.shared"
suite_file_name = "TestAll.java"
suite_class_name = "TestAll"

test_case_suffix = "Test.java"

is_name_of_test = lambda x: True if x.endswith(test_case_suffix) else False

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

file_header = """/*
 * created automatically; ALL MANUAL CHANGES TO THIS FILE WILL LIKELY BE OVERWRITTEN!
 * on %s
 * %s
 */
"""
def create_leading_comment():
	now = datetime.datetime.now()
	return file_header % (now.strftime('%a %Y-%m-%d %I:%M:%S %p %z'), sys.argv[0])

def path_to_classname(path):
	if path.startswith(suite_base_dir):
		path = path[len(suite_base_dir):]
	else:
		raise ValueError(path + " doesn't start with " + suite_base_dir)
	if path.endswith(".java"):
		path = path[:len(path)-len(".java")]
	classname = suite_base_package + path.replace(os.path.sep, ".") + ".class"
	return classname

def file_selector(arg, dirname, fnames):
	filtered = [os.path.join(dirname, name) for name in fnames if is_name_of_test(name)]
	arg += [filename for filename in filtered if os.path.isfile(filename)]

def collect_test_classfiles(dir):
	filelist = []
	os.path.walk(dir, file_selector, filelist)
	return filelist
	

def write_suite_file(classfiles):
	f = open(os.path.join(suite_base_dir, suite_file_name), "w")
	f.write(create_leading_comment())
	f.write(suite_header)
	for classname in [path_to_classname(x) for x in collect_test_classfiles(suite_base_dir)]:
		f.write("\t" + classname + ",\n")
	f.write(suite_footer)
	f.close()

write_suite_file(collect_test_classfiles(suite_base_dir))