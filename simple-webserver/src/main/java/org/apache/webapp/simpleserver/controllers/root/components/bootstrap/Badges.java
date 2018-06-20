/*
 * Copyright 2018 Niels Gron and Contributors All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.webapp.simpleserver.controllers.root.components.bootstrap;

import org.apache.webapp.simpleui.bootstrap4.components.BSBadges;
import org.apache.webapp.simpleui.bootstrap4.components.BSPanel;
import org.apache.webapp.simpleui.bootstrap4.components.BSText;

public class Badges extends AbstractComponentsBootstrapPage {

    public Badges() {
        getSideBar().setActiveItem("Badges");
    }

    protected BSPanel createContentPanelCenter() {

        BSPanel panel = new BSPanel();

        panel.add(new BSText("<br>"));
        panel.add(new BSText("List of Badges Components"));

        panel.add(new BSText("<br>"));
        panel.add(new BSText("<br>"));

        panel.add(new BSBadges("Default"));
        panel.add(new BSBadges("Primary", BSBadges.PRIMARY));
        panel.add(new BSBadges("Secondary", BSBadges.SECONDARY));
        panel.add(new BSBadges("Sucess", BSBadges.SUCCESS));
        panel.add(new BSBadges("Danger", BSBadges.DANGER));
        panel.add(new BSBadges("Warning", BSBadges.WARNING));
        panel.add(new BSBadges("Info", BSBadges.INFO));
        panel.add(new BSBadges("Light", BSBadges.LIGHT));
        panel.add(new BSBadges("Dark", BSBadges.DARK));

        panel.add(new BSText("<br>"));

        return panel;
    }

}
