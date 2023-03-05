package com.marjannnnn.approdite

class Project {
    var id: String? = null
    var projectName: String? = null
    var taskName: String? = null

    constructor() {}

    constructor(id: String?, projectName: String?, taskName: String?) {
        this.id = id
        this.projectName = projectName
        this.taskName = taskName
    }
}
