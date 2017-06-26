Before start using this security package we need to download the "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8" from oracle.com.
You will get jce_policy-8.zip
and we need to replace 
local_policy.jar and US_export_policy.jar
at /home/user1/jre1.8.0               [Unix]
        C:\jre1.8.0                        [Windows]


----------------------------------------------------------------------
Installation
----------------------------------------------------------------------

Notes:

  o Unix (Solaris/Linux/Mac OS X) and Windows use different pathname
    separators, so please use the appropriate one ("\", "/") for your
    environment.

  o <java-home> (below) refers to the directory where the JRE was
    installed. It is determined based on whether you are running JCE
    on a JRE or a JRE contained within the Java Development Kit, or
    JDK(TM). The JDK contains the JRE, but at a different level in the
    file hierarchy. For example, if the JDK is installed in
    /home/user1/jdk1.8.0 on Unix or in C:\jdk1.8.0 on Windows, then
    <java-home> is:

        /home/user1/jdk1.8.0/jre           [Unix]
        C:\jdk1.8.0\jre                    [Windows]

    If on the other hand the JRE is installed in /home/user1/jre1.8.0
    on Unix or in C:\jre1.8.0 on Windows, and the JDK is not
    installed, then <java-home> is:

        /home/user1/jre1.8.0               [Unix]
        C:\jre1.8.0                        [Windows]

  o On Windows, for each JDK installation, there may be additional
    JREs installed under the "Program Files" directory. Please make
    sure that you install the unlimited strength policy JAR files
    for all JREs that you plan to use.


Here are the installation instructions:

1)  Download the unlimited strength JCE policy files.

2)  Uncompress and extract the downloaded file.

    This will create a subdirectory called jce.
    This directory contains the following files:

        README.txt                   This file
        local_policy.jar             Unlimited strength local policy file
        US_export_policy.jar         Unlimited strength US export policy file

3)  Install the unlimited strength policy JAR files.

    In case you later decide to revert to the original "strong" but
    limited policy versions, first make a copy of the original JCE
    policy files (US_export_policy.jar and local_policy.jar). Then
    replace the strong policy files with the unlimited strength
    versions extracted in the previous step.

    The standard place for JCE jurisdiction policy JAR files is:

        <java-home>/lib/security           [Unix]
        <java-home>\lib\security           [Windows]

