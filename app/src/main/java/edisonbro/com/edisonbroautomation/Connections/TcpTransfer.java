package edisonbro.com.edisonbroautomation.Connections;


/**
 *  FILENAME: TcpTransfer.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Interface class used  to read data from tcp.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


/**
 * Created by Divya on 15-06-2017.
 */

public interface TcpTransfer {

    void read(final int type, final String stringData, final byte[] byteData);


}
