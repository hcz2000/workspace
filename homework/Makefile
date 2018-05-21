#CXXFLAGS =	-O2 -g -Wall -fmessage-length=0 -I .\include
CXXFLAGS =	-fmessage-length=0 -I .\include

SRC = src\homework.cpp

OBJS =	src\homework.o

LIBS =  lib\libopencv_world320.dll.a

TARGET =	bin\homework.exe

%.o:%.cpp
	$(CXX) -c $(CXXFLAGS) $< -o $@

$(TARGET):	$(OBJS)
	$(CXX) -o $(TARGET) $(OBJS)  $(LIBS)

all:	$(TARGET)

clean:
	rm -f $(OBJS) $(TARGET)
