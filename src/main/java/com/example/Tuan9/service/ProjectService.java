package com.example.Tuan9.service;

import com.example.Tuan9.entity.ProjectEntity;
import com.example.Tuan9.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public ProjectEntity createProject(ProjectEntity project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new RuntimeException("Lỗi: Tên dự án không được để trống!");
        }
        return projectRepository.save(project);
    }

    public ProjectEntity getProjectById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy dự án với ID " + id));
    }
}