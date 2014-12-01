package blogr.vpm.fr.blogr.persistence;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.bean.GithubBlog;

/**
 * Manages the files for the blogs
 *
 * Created by vince on 01/12/14.
 */
public class FileBlogManager implements BlogRetriever, BlogSaver {

  public static final String BLOGS_DIR = "blogs";
  public static final String APP_DIR = "BlogR";
  public static final String EXT_PROPS = ".properties";

  @Override
  public List<Blog> retrieveAll() throws IOException {
    File blogsDir = getBlogsDir();
    List<Blog> blogs = new ArrayList<Blog>();
    if (blogsDir.exists() && blogsDir.isDirectory()) {
      for (File blogFile : blogsDir.listFiles()) {
        if (blogFile.getPath().contains(EXT_PROPS)) {
          blogs.add(getBlogFromMetadata(blogFile));
        }
      }
    }
    return blogs;
  }

  @Override
  public boolean persist(Blog blog) throws IOException {
    Properties blogProps = getBlogProperties(blog);
    FileOutputStream blogStream = new FileOutputStream(getBlogFile(blog));
    blogProps.store(blogStream, "updating blog metadata");
    blogStream.close();
    return true;
  }

  private Properties getBlogProperties(Blog blog) {
    Blog.Storage blogPropCreator = null;
    if (blog instanceof EmailBlog){
      blogPropCreator = new EmailBlog.Storer();
    } else if (blog instanceof GithubBlog){
      blogPropCreator = new GithubBlog.Storer();
    }
    assert blogPropCreator != null;
    return blogPropCreator.marshall(blog);
  }

  /**
   * Extracts information from a Blog metadata file (a text file "<BlogTitle>.properties" in the blogs directory)
   * @param blogFile the metadata file for the blog
   * @return a blog
   * @throws IOException if there is any issue related to I/O
   */
  private Blog getBlogFromMetadata(File blogFile) throws IOException {
    // extract properties from file
    FileInputStream blogStream = new FileInputStream(blogFile);
    Properties props = new Properties();
    props.load(blogStream);
    blogStream.close();

    // create blog from properties
    String type = props.getProperty(Blog.Storage.TYPE_KEY);
    Blog blog = null;
    if (EmailBlog.class.getName().equals(type)) {
      blog = new EmailBlog.Storer().unmarshall(props);
    } else if (GithubBlog.class.getName().equals(type)) {
      blog = new GithubBlog.Storer().unmarshall(props);
    }
    return blog;
  }

  /**
   * Determines (and create is non-existent) the file for a blog (a text file "<BlogTitle>.properties" in the blogs directory)
   * @param blog the blog to get the file for
   * @return a property file to store metadata
   * @throws IOException if there is any issue related to I/O
   */
  private File getBlogFile(Blog blog) throws IOException {
    File blogsDir = getBlogsDir();
    File blogMetadataFile = new File(blogsDir, blog.getTitle() + EXT_PROPS);
    if (!blogMetadataFile.exists()){
      blogMetadataFile.createNewFile();
    }
    return blogMetadataFile;
  }

  /**
   * Determines (and create if non-existent) the directory file where blog metadata files are
   * @return an existing file and directory
   */
  private File getBlogsDir() {
    File blogsDir = new File(Environment.getExternalStoragePublicDirectory(APP_DIR), BLOGS_DIR);
    blogsDir.mkdirs();
    return blogsDir;
  }
}
