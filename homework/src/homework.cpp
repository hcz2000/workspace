//============================================================================
// Name        : homework.cpp
// Author      : HCZ
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C, Ansi-style
//============================================================================

#include <opencv2\opencv.hpp>
#include <iostream>
using namespace cv;
using namespace std;
int main() {
    Mat img = imread("D:/work/git/homework/pic/origin.gif");
    if (img.empty())
        cout << "cannot load image" << endl;
    imshow("Origin", img);
    Mat mask(img.rows, img.cols,CV_8UC3, Scalar(0, 0,0 ));
    circle(mask, Point(img.rows / 2, img.cols / 2-35), 220,Scalar(255,255,255),-1);  //»­Ò»¸öÔ²
    imshow("Mask", mask);
    Mat r;
    bitwise_and(img, mask, r);
    imshow("Bit_and", r);
    waitKey(0);
    return 0;
}
