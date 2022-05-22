package com.ss.android.ugc.aweme.cao.plugin.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.ss.android.ugc.aweme.cao.plugin.CaoInstance

class HelpAction: BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse("https://bytedance.feishu.cn/docx/doxcnvxySLz2foHABIVhX7tV5yc")
    }

}