/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.mail.handlers;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

/**
 * Base class for other DataContentHandlers.
 */
public abstract class handler_base implements DataContentHandler {

    /**
     * Return an array of ActivationDataFlavors that we support.
     * Usually there will be only one.
     *
     * @return array of ActivationDataFlavors that we support
     */
    protected abstract ActivationDataFlavor[] getDataFlavors();

    /**
     * Given the flavor that matched, return the appropriate type of object.
     * Usually there's only one flavor so just call getContent.
     *
     * @param aFlavor the ActivationDataFlavor
     * @param ds      DataSource containing the data
     * @return the object
     * @throws IOException for errors reading the data
     */
    protected Object getData(ActivationDataFlavor aFlavor, DataSource ds)
            throws IOException {
        return getContent(ds);
    }

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        ActivationDataFlavor[] adf = getDataFlavors();
        if (adf.length == 1)    // the common case
            return new DataFlavor[]{adf[0]};
        DataFlavor[] df = new DataFlavor[adf.length];
        System.arraycopy(adf, 0, df, 0, adf.length);
        return df;
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ds The DataSource corresponding to the data
     * @return the object
     * @throws IOException for errors reading the data
     */
    @Override
    public Object getTransferData(DataFlavor df, DataSource ds)
            throws IOException {
        ActivationDataFlavor[] adf = getDataFlavors();
        for (int i = 0; i < adf.length; i++) {
            // use ActivationDataFlavor.equals, which properly
            // ignores Content-Type parameters in comparison
            if (adf[i].equals(df))
                return getData(adf[i], ds);
        }
        return null;
    }
}
