/*
 * Copyright (c) 2012 Stefan Wolf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package hudson.plugins.depgraph_view.model.graph;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Preconditions;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;

/**
 * A Node in the DependencyGraph, which corresponds to a Project
 */
public class ProjectNode {
    private final AbstractProject<?,?> project;

    public static ProjectNode node(AbstractProject<?, ?> project) {
        return new ProjectNode(project);
    }

    public ProjectNode(AbstractProject<?, ?> project) {
        Preconditions.checkNotNull(project);
        this.project = project;
    }

    public String getName() {
        return project.getFullDisplayName();
    }

    public AbstractProject<?, ?> getProject() {
        return project;
    }
    
    // Color names / hex can be found here: http://www.computerhope.com/htmcolor.htm
    public String getColor(boolean projColor) {
    	
    	if( projColor ){
    		if( getName().matches("db-.*") || getName().matches(".*db-data.*")){
    			return "lightblue";
    		}
    		else if(getName().matches(".*perl.*")){
    			return "brown";
    		}
    		else if(getName().matches("corefe-.*") || getName().matches("corebe-.*")){
    			return "yellow";
    		}
    		else if(getName().matches("deploy-.*")){
    			return "forestgreen";
    		}
    	}
    	else{
    		try{
        		AbstractBuild<?,?> build = project.getLastBuild();
        		if( !project.isBuildable() ){
        			return "gray";
        		}
        		else if( build.isBuilding() ){
        			return "lightblue";
        		}
        		else if( project.isInQueue() ) {
        			return "#FFFFCC";  // "cream" color
        		}
        		else if( has_custom_error_flag( build ) ){
        			return "orange";
        		}
        		else if( build.getResult() == Result.SUCCESS){
            		return "green";
            	}
            	else if( build.getResult() == Result.FAILURE){
            		return "red";
            	}
            	else if( build.getResult() == Result.UNSTABLE){
            		return "yellow";
            	}
            	else if( build.getResult() == Result.ABORTED){
            		return "orange";
            	}
        		
        	}
        	catch( Exception e){
        		return "white";
        	}
    	}
    	   	
        return "white";
    }
    
    public boolean has_custom_error_flag(AbstractBuild<?, ?> build) throws IOException, InterruptedException {
    	FilePath ws = build.getWorkspace();
		FilePath file = new FilePath(ws, "jenk_error");
		
		return build.getWorkspace().list().contains(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectNode that = (ProjectNode) o;

        if (!project.equals(that.project)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return project.hashCode();
    }
}
