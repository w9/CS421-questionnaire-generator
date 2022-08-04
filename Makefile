_public_dev:
	mkdir -p public_dev && cd public_src && npx ejs index.ejs.html > ../public_dev/index.html
	rsync -av static_files/ public_dev/

_public:
	mkdir -p public && cd public_src && npx ejs index.ejs.html | npx html-minifier-terser --collapse-whitespace --remove-comments --minify-js true > ../public/index.html
	rsync -av static_files/ public/

_update-can-i-use:
	npx browserslist@latest --update-db

_dev: _public_dev _update-can-i-use
	env \
		BASEPATH=${HOME}/.local/var/lib/covepitope_dev \
		LOG_PATH=${HOME}/.local/var/lib/covepitope_dev/log \
		PORT=3000 \
		npx shadow-cljs watch :app

_antq:
	clojure -Tantq outdated :check-clojure-tools true :upgrade true

_release-static-to-nginx: _public
	sudo rm -rvf /var/www \
  && sudo mkdir -p /var/www \
  && sudo chown nobody:nobody /var/www \
  && sudo rsync -av --chown nobody:nobody ./public/ /var/www/public/ \
  && sudo rsync -v ./nginx.conf /etc/nginx/nginx.conf \
  && sudo nginx -s reload
