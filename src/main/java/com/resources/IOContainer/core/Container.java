package com.resources.IOContainer.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.collect.Sets;
import com.resources.IOContainer.Exception.UndefinedInjectableException;
import com.resources.IOContainer.annotations.Inject;
import com.resources.IOContainer.annotations.Injectable;

public class Container{
	
	private static Package pkg;
	private static Set<Class<?>> packageClasses;
	private Set<Class<? extends Object>> annotatedClasses;
	private static Map<String, Object> appContext = new HashMap<String, Object>();
	
	public Container() {
	}

	public String getPkg() {
		return pkg.getName();
	}

	public static void setPkg(Package entryPackage) {
		pkg = entryPackage;
	}

	public Set<Class<? extends Object>> getAnnotatedClasses() {
		return annotatedClasses;
	}

	public Map<String, Object> getAppContext() {
		return appContext;
	}
	
	public void scanComponents(Class<?> cls) throws ClassNotFoundException {
		if(cls == null) throw new ClassNotFoundException("Entry point class not found");
			
		
	}
	
	public static Set<Class<?>> getPackageClasses(){
		return packageClasses;
	}
	
	public static void scanAndSetPackageClasses(){
		// configure reflections to scan include classes of type object...
				List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
				classLoadersList.add(ClasspathHelper.contextClassLoader());
				classLoadersList.add(ClasspathHelper.staticClassLoader());
				
				Reflections reflections = new Reflections(new ConfigurationBuilder()
					    .setScanners(new SubTypesScanner(false), new ResourcesScanner(), new TypeElementsScanner())
					    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
					    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pkg.getName()))));
				
				Set<String> typeSet = reflections.getStore().get("TypeElementsScanner").keySet();
				HashSet<Class<? extends Object>> classes = Sets.newHashSet(ReflectionUtils.forNames(typeSet, reflections
				            .getConfiguration().getClassLoaders()));
				//Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
				//Stream<Class<?>> streamOfClasses = classes.stream();
				packageClasses = classes;
	}
	
	
	// scan all classes if they are annotated as injectables...
	public static void catchAndRegisterInjectables() {
	
		Stream<Class<?>> allClasses = getPackageClasses().stream();
		 allClasses
				.filter(cls -> cls.isAnnotationPresent(Injectable.class))
				.forEach(cls -> {
					try {
						Object o =  Class.forName(cls.getName()).getConstructor().newInstance();
						appContext.put(cls.getCanonicalName(), o);
						System.out.println(cls.getCanonicalName());
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
	}
	

	
	// scan class fields annotated with inject interface...
	public static void injectAnnotatedClassFields(Class<?> cls) {
		// get declared fields of the class..
		try {
			Class<?> loadedClass = Class.forName(cls.getName());
			Field[] fields = loadedClass.getDeclaredFields();
			// loop inside declared fields of class..
			for(Field field: fields) {
				System.out.println(field.getName());
			// check if the field is annotated with the inject interface..
				if(field.isAnnotationPresent(Inject.class)) {
				String fieldType = field.getType().getName();
						try {
							if(appContext.get(fieldType) != null) {
		// If true, set matching field as accessible and set the object in the context as the value..
								Object instance = appContext.get(fieldType);
								field.setAccessible(true);
								try {
									field.set(loadedClass, instance);
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else {
								throw new UndefinedInjectableException("Field type is not registered in the context.");
							}
						}catch(UndefinedInjectableException e) {
							e.printStackTrace();
						}
				}
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
	}
	
	
	public static void catchAndInject() {
		Stream<Class<?>> allClasses = getPackageClasses().stream();
			allClasses.forEach( cls -> {
				try {
					Class<?> injectedClass = Class.forName(cls.getName());
					injectAnnotatedClassFields(injectedClass);

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	}
	
	public static void run(Class<?> entryPoint) {
		setPkg(entryPoint.getPackage());
		scanAndSetPackageClasses();
		catchAndRegisterInjectables();
		catchAndInject();
		
	}

}
