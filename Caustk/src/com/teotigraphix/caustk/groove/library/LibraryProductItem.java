
package com.teotigraphix.caustk.groove.library;

import java.util.UUID;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public abstract class LibraryProductItem extends LibraryItem {

    @Tag(10)
    private UUID productId;

    @Tag(11)
    private String productPath;

    public UUID getProductId() {
        return productId;
    }

    /**
     * Returns the relative base of the file within the product.
     * <p>
     * E.g <code>/Groups/ALLEY 01.ggrp</code>
     */
    public String getProductPath() {
        return productPath;
    }

    public void setProductPath(String productPath) {
        this.productPath = productPath;
    }

    public LibraryProductItem() {
    }

    public LibraryProductItem(UUID productId) {
        this.productId = productId;
    }

}
