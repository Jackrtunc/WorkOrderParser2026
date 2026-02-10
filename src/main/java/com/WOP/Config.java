package com.WOP;

import com.WOP.view.help.HelpPageConfig;
import com.WOP.workorder.config.DepartmentConfig;
import com.WOP.workorder.config.FacilitiesConfig;

public record Config(
    DepartmentConfig departmentConfig,
    FacilitiesConfig facilitiesConfig,
    HelpPageConfig helpPageConfig) {}
