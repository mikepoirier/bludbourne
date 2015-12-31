package com.packtpub.libgdx.bludbourne;

/**
 * Created by michael.poirier on 12/31/2015.
 */
public interface Component {
    void update(Entity entity, float delta);

    void dispose();
}
