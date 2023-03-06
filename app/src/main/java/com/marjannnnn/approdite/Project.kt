package com.marjannnnn.approdite

class Project {
    var id: Int? = null
    var projectName: String? = null
    var taskName: String? = null

    constructor() {}

    constructor(id: Int?, projectName: String?, taskName: String?) {
        this.id = id
        this.projectName = projectName
        this.taskName = taskName
    }
}
