
CREATE TABLE locations(
	id BIGSERIAL PRIMARY KEY,
	city TEXT NOT NULL,
	country TEXT NOT NULL,
	picture TEXT NOT NULL
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    fullname TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    avatar TEXT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT users_role_fk FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE trips(
	id BIGSERIAL PRIMARY KEY,
	user_id BIGINT NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL,
	CONSTRAINT trip_fk FOREIGN KEY(user_id) REFERENCES users(id)

);

CREATE TABLE activities(
	id BIGSERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	cost NUMERIC NOT NULL,
	duration INTEGER NOT NULL,
	location_id BIGINT NOT NULL,
	CONSTRAINT activities_fk FOREIGN KEY(location_id) REFERENCES locations(id)

);

CREATE TABLE events(
	id BIGSERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	image TEXT NOT NULL,
	date DATE NOT NULL,
	cost NUMERIC NOT NULL,
	duration INTEGER NOT NULL,
	location_id BIGINT NOT NULL,
	CONSTRAINT events_fk FOREIGN KEY(location_id) REFERENCES locations(id)
);

CREATE TABLE followers(
	id BIGSERIAL PRIMARY KEY,
	user_id BIGINT NOT NULL,
	follower_id BIGINT NOT NULL,
	CONSTRAINT followers_user_fk FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT followers_follower_fk FOREIGN KEY(follower_id) REFERENCES users(id)

);

CREATE TABLE recommendations(
	id BIGSERIAL PRIMARY KEY,
	description TEXT NOT NULL,
	user_id BIGINT NOT NULL,
	activity_id BIGINT NOT NULL,
	CONSTRAINT recommendations_user_fk FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT recommendations_activity_fk FOREIGN KEY(activity_id) REFERENCES activities(id)
);

CREATE TABLE posts(
	id BIGSERIAL PRIMARY KEY,
	text TEXT NOT NULL,
	image TEXT NOT NULL,
	user_id BIGINT NOT NULL,
	posted_time DATE NOT NULL,
	trip_id BIGINT NOT NULL,
	CONSTRAINT posts_user_fk FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT posts_trip_fk FOREIGN KEY(trip_id) REFERENCES trips(id)
);

CREATE TABLE reviews(
	id BIGSERIAL PRIMARY KEY,
	description TEXT NOT NULL,
	rating BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	locations_id INTEGER NOT NULL,
	CONSTRAINT reviews_user_fk FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT reviews_location_fk FOREIGN KEY(locations_id) REFERENCES locations(id)

);

CREATE TABLE news(
	id BIGSERIAL PRIMARY KEY,
	author_id BIGINT NOT NULL,
	title TEXT NOT NULL,
	description TEXT NOT NULL,
	picture TEXT NOT NULL,
	CONSTRAINT news_fk FOREIGN KEY(author_id) REFERENCES users(id)

);

CREATE TABLE hotels(
	id BIGSERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	location_id BIGINT NOT NULL,
	rating NUMERIC NOT NULL,
	booking_link TEXT NOT NULL,
	CONSTRAINT hotels_fk FOREIGN KEY(location_id) REFERENCES locations(id)

);

CREATE TABLE bookmarks(
	id BIGSERIAL PRIMARY KEY,
	location_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT bookmarks_location_fk FOREIGN KEY(location_id) REFERENCES locations(id),
	CONSTRAINT bookmarks_user_fk FOREIGN KEY(user_id) REFERENCES users(id)

);

CREATE TABLE flights(
	id BIGSERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	origin_id BIGINT NOT NULL,
	destination_id BIGINT NOT NULL,
	time TIMESTAMP NOT NULL,
	CONSTRAINT flights_origin_fk FOREIGN KEY(origin_id) REFERENCES locations(id),
	CONSTRAINT flights_destination_fk FOREIGN KEY(destination_id) REFERENCES locations(id)

);


CREATE TABLE local_cuisine(
	id BIGSERIAL PRIMARY KEY,
	name TEXT NOT NULL,
	description TEXT NOT NULL,
	image TEXT NOT NULL,
	location_id BIGINT NOT NULL,
	CONSTRAINT local_cuisine_fk FOREIGN KEY(location_id) REFERENCES locations(id)
);


CREATE TABLE items(
	id BIGSERIAL PRIMARY KEY,
	item_name TEXT NOT NULL
);

CREATE TABLE trip_items(
	id BIGSERIAL PRIMARY KEY,
	trip_id BIGINT NOT NULL,
	item_id BIGINT NOT NULL,
	CONSTRAINT trip_items_trips_fk FOREIGN KEY(trip_id) REFERENCES trips(id),
	CONSTRAINT trip_items_items_fk FOREIGN KEY(item_id) REFERENCES items(id)
);

CREATE TABLE trip_activity_list(
	id BIGSERIAL PRIMARY KEY,
	trip_id BIGINT NOT NULL,
	activity_id BIGINT NOT NULL,
	planned_date DATE NOT NULL,
	visit_order INTEGER NOT NULL,
	CONSTRAINT trip_activity_list_trips_fk FOREIGN KEY(trip_id) REFERENCES trips(id),
	CONSTRAINT trip_activity_list_activities_fk FOREIGN KEY(activity_id) REFERENCES activities(id)
);

CREATE TABLE trip_event_list(
	id BIGSERIAL PRIMARY KEY,
	trip_id BIGINT NOT NULL,
	event_id BIGINT NOT NULL,
	planned_date DATE NOT NULL,
	visit_order SERIAL NOT NULL,
	CONSTRAINT trip_event_list_trips_fk FOREIGN KEY(trip_id) REFERENCES trips(id),
	CONSTRAINT trip_event_list_events_fk FOREIGN KEY(event_id) REFERENCES events(id)
);

CREATE TABLE trip_place_list(
	id BIGSERIAL PRIMARY KEY,
	trip_id BIGINT NOT NULL,
	location_id BIGINT NOT NULL,
	planned_date DATE NOT NULL,
	visit_order SERIAL NOT NULL,
	CONSTRAINT trip_place_list_trips_fk FOREIGN KEY(trip_id) REFERENCES trips(id),
	CONSTRAINT trip_place_list_locations_fk FOREIGN KEY(location_id) REFERENCES locations(id)
);