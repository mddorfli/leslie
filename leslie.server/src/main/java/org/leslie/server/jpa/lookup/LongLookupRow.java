package org.leslie.server.jpa.lookup;

import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

/**
 * A Long-typed implementaiton of {@link LookupRow}. Fields and types are as
 * follows:<br>
 * <code>
 * Long id<br>
 * String text <br>
 * String iconId <br>
 * String tooltip <br>
 * String background color <br>
 * String foreground color <br>
 * String font <br>
 * Boolean enabled <br>
 * Object parentKey used in hierarchical structures to point to the parents
 * primary key <br>
 * Boolean active (0,1) see {@link TriState#parse(Object)}
 * </code>
 * 
 * @author Marco Dörfliger
 *
 */
public class LongLookupRow extends LookupRow<Long> {

    private static final long serialVersionUID = -5656586143007910178L;

    public LongLookupRow(Long key, String text) {
	super(key, text);
    }

    /**
     * Creates a new lookup row with the given cells as data.
     *
     * @param cells
     *            array containing the following values:<br>
     *            Object key (use keyClass to specify the type of the key) <br>
     *            String text <br>
     *            String iconId <br>
     *            String tooltip <br>
     *            String background color <br>
     *            String foreground color <br>
     *            String font <br>
     *            Boolean enabled <br>
     *            Object parentKey used in hierarchical structures to point to
     *            the parents primary key <br>
     *            Boolean active (0,1) see {@link TriState#parse(Object)}
     */
    public LongLookupRow(Object... cells) {
	super(cells, Long.class);
    }
}
