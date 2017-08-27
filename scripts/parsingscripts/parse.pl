#!/usr/local/bin/perl -w

use strict;

use Carp;
use FileHandle;
use File::Find;


open (FILE, 'data.txt');

while (<FILE>) {
	chomp;
	#print $_;
	my @lines = split(' ', $_);
	if (scalar(@lines) > 0) {
		if ( $lines[0] =~ /^[A-Z]{3}$/){
			print "GOT EM $_\n";
			my $nextline;
			$nextline = <FILE>;
			if ($nextline =~ /^[0-9].cr/){
				$nextline = <FILE>;
				print lc($nextline), "\n";
			}
			else {
				print lc($nextline), "\n";
			}
		}
	}
}

close (FILE);
exit;
