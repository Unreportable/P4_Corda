# P4_Corda

Simple Corda apllication with 5 nodes for an upgrading exam mark for 4th year course "Basics of Grid and Cloud Computing"

# a short unique description

Can you imagine a world without drivers who always break the traffic laws?

Drivers Police Departments or GIBDDs surely have to fine those law breakes!

Each GIBDD can give a ticket for bad-behavioured drivers.

The information, provided by officers are: drivers name, violation and the fine they should to pay for it.

Also, drivers can check their notices of violation form different GIBDDs

# a gif file of how your project works

It's hard to make a decent animation from the whole proccess according to its not-suitable look.

Lets have a glance at some pictures:

The opened nodes:

![](https://github.com/Unreportable/P4_Corda/blob/master/picture/1.jpg)


For adding tickets you have to connect with needed ports.

For example with these default parameteres: --server.port=10050 —config.rpc.host=localhost —config.rpc.port=10011 —config.rpc.username=user1 —config.rpc.password=test

Then send a requests:
![](https://github.com/Unreportable/P4_Corda/blob/master/picture/addeast.jpg)
![](https://github.com/Unreportable/P4_Corda/blob/master/picture/addwest.jpg)

After all tickets are given, it is possible to check them from different persons:
1) drivers:
![](https://github.com/Unreportable/P4_Corda/blob/master/picture/DriverWick.jpg)
![](https://github.com/Unreportable/P4_Corda/blob/master/picture/driver_john.jpg)
2) departments:

![](https://github.com/Unreportable/P4_Corda/blob/master/picture/DepartmentEast.jpg)
![](https://github.com/Unreportable/P4_Corda/blob/master/picture/DepartmentWest.jpg)


# a short instruction on how to build and run your project

Gradle and SpringBoot should be installed.

For deploying you have to follow this steps:
1) Download all files from github repository
2) From Gradle make tasks following this order: clean-build-jar-deployNodes 
3) Run runnodes.but and wait for all nodes to be deployed
4)Change Program arguements for required node as it was shown in the example before (you can check nodes ports from recently opened corda terminals)

