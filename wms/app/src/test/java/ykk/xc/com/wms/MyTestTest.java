package ykk.xc.com.wms;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyTestTest {

    @Test
    public void main() {
        String a = "KJDJ:(KF:JD:65:85:65)";
        System.out.print(a.substring(a.indexOf("(")+1, a.indexOf(")")));
    }
}