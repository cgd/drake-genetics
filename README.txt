NOTE: this application is still in the very early stages of development and is
not yet suitable for use.

DrakeGenetics is an open sourced, educational web application which is being
developed by The Center for Genome Dynamics at The Jackson Laboratory in order
to help students learn about genetics.

WARRANTY DISCLAIMER AND COPYRIGHT NOTICE
========================================

The Jackson Laboratory makes no representation about the suitability or accuracy
of this software for any purpose, and makes no warranties, either express or
implied, including merchantability and fitness for a particular purpose or that
the use of this software will not infringe any third party patents, copyrights,
trademarks, or other rights. The software are provided "as is".

This software is provided to enhance knowledge and encourage progress in the
scientific community. This is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version."

BUILDING
========

In order successfully complete these build instructions you must have:
* git
* ant >= 1.7.1
* java development kit (JDK) >= 1.5

To obtain and build the source code enter the following commands (where
GIT_URL is git://github.com/cgd/drake-genetics.git if you are not a member of
this project and git@github.com:cgd/drake-genetics.git if you are a member of
this project):
    git clone GIT_URL
    cd drake-genetics
    ./init-git-submodules.sh
    ant

This will build a war file under `./modules/drake-genetics-server/dist/` which
you can deploy using a java application server such as tomcat.
