/*
 * This file is part of herbmarshall.com: require.library  ( hereinafter "require.library" ).
 *
 * require.library is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 2 of the License,
 * or (at your option) any later version.
 *
 * require.library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with require.library.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.herbmarshall.require;

import java.util.Set;

/**
 * Module to provide data assertions for {@link Set} values.
 * @param <E> The {@link Set} element type
 */
public final class RequireSet<E>
	extends RequireCollection<E, Set<E>, RequireSetFaultBuilder<E>, RequireSet<E>> {

	RequireSet( Set<E> actual ) {
		super( actual, new RequireSetFaultBuilder<>( actual ) );
	}

}
