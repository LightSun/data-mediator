/*
 * Copyright (C) 2013 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.plugin.idea.data_mediator;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.panels.VerticalBox;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GenerateDialog extends DialogWrapper {

    private final LabeledComponent<JPanel> fieldsComponent;
    private JTextArea mLabel;

    protected GenerateDialog(final PsiClass psiClass) {
        super(psiClass.getProject());
        setTitle("Select Fields for Parcelable Generation");

        mLabel = new JTextArea();
        final JBList fieldList = new JBList( new CollectionListModel<PsiField>());
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        final ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList).disableAddAction();
        final JPanel panel = decorator.createPanel();

        fieldsComponent = LabeledComponent.create(panel, "Fields to include in Parcelable");
        fieldsComponent.add(mLabel);
    }

    public void show(Object ...objs){
        if(objs != null){
            for (Object obj : objs){
                mLabel.append(obj != null ? obj.toString() : null);
                mLabel.append("\r\n");
            }
        }
        show();
    }
    /**
     * Exclude static fields.
     */
    private List<PsiField> getClassFields(PsiField[] allFields) {
        final List<PsiField> fields = new ArrayList<PsiField>();
        for (PsiField field : allFields) {
            if (!field.hasModifierProperty(PsiModifier.STATIC) && !field.hasModifierProperty(PsiModifier.TRANSIENT)) {
                fields.add(field);
            }
        }
        return fields;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return fieldsComponent;
    }

    @Nullable
    @Override
    protected JComponent createSouthPanel() {
        JComponent southPanel = super.createSouthPanel();
        if(southPanel != null) {
            final VerticalBox combinedView = new VerticalBox();
            combinedView.add(fieldsComponent);
            combinedView.add(southPanel);
            return combinedView;
        } else {
            return southPanel;
        }
    }
}
