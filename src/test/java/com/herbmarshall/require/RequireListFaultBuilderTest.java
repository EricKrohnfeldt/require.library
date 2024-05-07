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

import java.util.List;
import java.util.UUID;

final class RequireListFaultBuilderTest
	extends RequireCollectionFaultBuilderTest<Object, List<Object>, RequireListFaultBuilder<Object>> {

	RequireListFaultBuilderTest() {
		super( RequireListFaultBuilder.COLLECTION_TYPE_NAME );
	}

	@Override
	protected RequireListFaultBuilder<Object> initializeFaultBuilder( List<Object> actual ) {
		return new RequireListFaultBuilder<>( actual );
	}

	@Override
	protected List<Object> randomValue() {
		return List.of( randomElement(), randomElement(), randomElement() );
	}


	@Override
	Object randomElement() {
		return UUID.randomUUID();
	}

}
