var through = require("through2");
var spawn = require("gulp-spawn");
var fs = require("fs");
var gulp = require("gulp");

// (b/compile-to-stream nil nil nil)
var cljsBuild = `(require '[lumo.build.gulp :as b]) (b/debug)`;

gulp.task('default', function () {
  gulp.src('gulp-test/testfile.cljs', {buffer: false})
    .pipe(spawn({cmd: "node",
    		 args: ["target/bundle",
			"-k",
			"lumo-cache",
			"-c",
			"target",
			"-",
			"-e",
			`(println "sadhshadas")`
			// cljsBuild
		       ]}))
    .pipe(gulp.dest('gulp-test/tmp'))});


// .pipe(spawn({cmd: "yarn",
//   	       args: ["dev",
// 		      "-",
// 		      "-e",
// 		      `"tesssz"`
// 		      // cljsBuild
// 		     ]}))

// .pipe(spawn({cmd: "node",
//   	       args: ["/home/hlolli/lumo/target/bundle.js",
//   		      "-",
//   		      "-k",
//   		      "/home/hlolli/lumo/lumo-cache",
//   		      "-",
//   		      "-c",
//   		      "/home/hlolli/lumo/target",
// 		      "-",
// 		      "-e",
// 		      `(println "Hello!!!")`]}))

// spawn({cmd: "node",
//        args: ["../target/bundle.js",
// 	      "-k",
// 	      "lumo-cache",
// 	      "-c",
// 	      "target",
// 	      "-e",
// 	      `(println "Hello!!!")`]})

