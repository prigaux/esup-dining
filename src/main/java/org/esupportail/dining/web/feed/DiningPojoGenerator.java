/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.dining.web.feed;

import java.io.File;
import java.net.URL;

import com.googlecode.jsonschema2pojo.SchemaMapper;
import com.sun.codemodel.JCodeModel;

public class DiningPojoGenerator {

	private final File jsonSchema = new File(
			"src/main/resources/schema/portlet-schema.json");
	private final File outputFolder = new File("src/main/java");

	public DiningPojoGenerator() throws Exception {
		// if(outputFolder.listFiles().length > 0)
		// this.deleteFilesFromFolder(outputFolder);
		// this.generatePOJO();
	}

	private void deleteFilesFromFolder(File folder) {
		for (File f : folder.listFiles()) {
			boolean success = f.delete();
			if (!success) {
				System.out.println(f + " couldn't be deleted.");
			}
		}
	}

	// Create Java Class based on the JSON Schema
	private void generatePOJO() {
		JCodeModel codeModel = new JCodeModel();
		// method give in the javadoc, looks dirty but recommanded
		URL jsonSchemaUrl;
		try {
			jsonSchemaUrl = this.jsonSchema.toURI().toURL();
			new SchemaMapper().generate(codeModel, "RestaurantFeedRoot",
					"org.esupportail.dining.web.models", jsonSchemaUrl);
			codeModel.build(this.outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}