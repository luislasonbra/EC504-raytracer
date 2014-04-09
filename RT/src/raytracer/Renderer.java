package raytracer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import scene.Scene;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4d;

import objects.Material;
import objects.SceneObject;
import objects.Sphere;
import scene.PointLight;
import scene.Scene;
import scene.Transformation;

/**
 * Ray tracing renderer, for EC504 at Boston University
 * based on the work of Rafael Martin Bigio <rbigio@itba.edu.ar.
 * 
 * 
 * @author Rana Alrabeh, Tolga Bolukbasi, Aaron Heuckroth, David Klaus, and Bryant Moquist
 */
public class Renderer
{

	private static boolean optionProgress = false;
	private static int optionAntialiasing = 1;
	private static String optionOutputFile;
	private static String optionInputFile;
	private static int optionWidth = 400;
	private static int optionHeight = 300;
	private static int optionShadow = 20;

	public static double progress = 0.0;
	public static boolean done = false;

	/* basic setup for rendering simple scene */
	public static void main(String[] args)
	{
		optionProgress = true;
		optionAntialiasing = 1;
		optionWidth = 400;
		optionHeight = 300;
		optionShadow = 20;
		showSampleScene();
	}

	public static void showSampleScene()
	{
		try
		{
			new RenderViewer(renderScene(constructSampleScene()));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setOptionProgress(boolean b)
	{
		optionProgress = b;
	}

	public static void setOptionAntialiasing(int i)
	{
		optionAntialiasing = i;
	}

	public static void setOptionOutputFile(String s)
	{
		optionOutputFile = s;
	}

	public static void setOptionInputFile(String s)
	{
		optionInputFile = s;
	}

	public static void setOptionWidth(int w)
	{
		optionWidth = w;
	}

	public static void setOptionHeight(int h)
	{
		optionHeight = h;
	}

	public static void setOptionShadow(int s)
	{
		optionShadow = 20;
	}

	public static Scene constructSampleScene()
	{
		

		SceneObject shape1,shape2,shape3;

		Material m1 = new Material();
		Material m2 = new Material();
		Material m3 = new Material();

		/* set poisitions and material of sphere objects */
		/* shape */

		/* shape1 */
		Vector3d scale = new Vector3d(1,1,1);
		Vector3d pos = new Vector3d(-50, -50, 0);
		AxisAngle4d rot = new AxisAngle4d(0,0,1,0);
		Transformation t1 = new Transformation(scale,pos,rot);
		shape1 = new Sphere(10f, -10f, 10f, 360f, t1);
		m1.diffuseColor = new Vector3d(0, 1, 0);
		m1.reflectionIndex = 0.5;
		shape1.getMaterial().set(m1);

		/* shape2 */
		scale = new Vector3d(1,1,1);
		pos = new Vector3d(-50, 30, 0);
		rot = new AxisAngle4d(0,0,1,0);
		Transformation t2 = new Transformation(scale,pos,rot);
		shape2 = new Sphere(25f, -102f, 250f, 200f,t2);
		m2.diffuseColor = new Vector3d(0, 0, 1);
		m2.reflectionIndex = 0.5;
		shape2.getMaterial().set(m2);

		/* shape3 */
		scale = new Vector3d(1,1,1);
		pos = new Vector3d(0, 30, -10);
		rot = new AxisAngle4d(0,0,1,0);
		Transformation t3 = new Transformation(scale,pos,rot);
		shape3 = new Sphere(10f, -10f, 10f, 360f,t3);
		m3.diffuseColor = new Vector3d(1, 0, 0);
		m3.reflectionIndex = 0.5;
		shape3.getMaterial().set(m3);

		/* set up camera */
		double fieldofView = 1;
		Point3d position = new Point3d(200, 0, 0);
		Point3d sphereLocation = new Point3d(0, 0, 0);
		Point3d takePos = new Point3d(position.x, position.y, position.z);
		Point3d takeSL = new Point3d(sphereLocation.x, sphereLocation.y, sphereLocation.z);
		takePos.sub(takeSL);

		Vector3d up = new Vector3d(0, 0, 1);

		/*
		 * uses axisangle to create camera (I hate axisangle) you will need to modify the
		 * position to get this to work it may be instructive to try to get this camera to
		 * work to test if you understand camera operations
		 */
		// Camera cam = new Camera(position, new AxisAngle4d(1,0,0,0.55), fieldofView);

		/* uses look at type constructor to create camera */
		Camera cam = new Camera(position, sphereLocation, up, fieldofView);
		// System.out.println(cam.toString()); //debug

		/* set lights */
		PointLight l1 = new PointLight();
		l1.setColor(new Vector3d(0.5, 1, 1));
		l1.setPosition(new Vector3d(-100, 0, 0));
		PointLight l2 = new PointLight();
		l2.setColor(new Vector3d(.2, 1, .2));
		l2.setPosition(new Vector3d(140, 200, 5));
		PointLight l3 = new PointLight();
		l3.setPosition(new Vector3d(1000, 0, 0));
		l3.setColor(new Vector3d(.65, .65, .65));
		PointLight l4 = new PointLight();
		l4.setColor(new Vector3d(.9, .9, .9));
		l4.setPosition(new Vector3d(50, 5, -5));
		l4.setPosition(new Vector3d(50, -20, -5));
		

		PointLight l5 = new PointLight();
		l5.setColor(new Vector3d(0, .5, 0));
		l5.setPosition(new Vector3d(14, 2, 5));
		PointLight l6 = new PointLight();
		l6.setPosition(new Vector3d(16, 0, 5));
		l6.setColor(new Vector3d(0, 0, 1));


		/* add objects & lights & camera to scene */
		Scene scene = new Scene();
		//scene.addSceneObject(shape0);
		scene.addSceneObject(shape1);
		scene.addSceneObject(shape2);
		scene.addSceneObject(shape3);
		scene.addLight(l1);
		scene.addLight(l2);
	    scene.addLight(l4);
		//scene.addLight(l3);
		//scene.addLight(l5);
		//scene.addLight(l6);
		scene.setCamera(cam);

		return scene;

	}

	/**
	 * Does the scene rendering. Create the ray tracer object called with the generated
	 * scene, and then visualize the rendered scene.
	 * 
	 * @throws IOException
	 *             if there are errors in the file access input/output
	 */
	public static BufferedImage renderScene(Scene scene) throws IOException
	{

		progress = 0.0;
		done = false;
		Dimension imageSize = new Dimension(optionWidth, optionHeight);

		/* Ultra simple raytracer */
		SimpleRayTracer rayTracer = new SimpleRayTracer(scene, imageSize, optionAntialiasing,
				optionShadow);


		BufferedImage result = rayTracer.render(optionProgress);
		return result;
	}
}
