$:.unshift File.join(File.dirname(__FILE__))
require 'bundler'
Bundler.require

require 'app'
run App