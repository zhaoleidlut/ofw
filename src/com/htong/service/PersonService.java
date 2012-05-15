package com.htong.service;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.data.document.mongodb.MongoTemplate;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;
import org.springframework.data.document.mongodb.query.Update;
import org.springframework.stereotype.Component;

import com.htong.domain.Person;

@Scope("prototype")
@Component
public class PersonService{
	private static Logger logger = Logger.getLogger(PersonService.class);

	@Resource(name = "mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * 获取所有的 person
	 */
	public List getAll() {
		// logger.debug(Retrieving all persons);

		// 查找一个属性（pid）存在的条目
		Query query = new Query(Criteria.where("pid").exists(true));

		// 执行查询，查找所有的存在的Person条目
		List persons = mongoTemplate.find(query, Person.class);

		return persons;
	}

	/**
	 * 获取一个 person
	 */
	public Person get(String id) {

		// 获取一个pid匹配id的条目
		Query query = new Query(Criteria.where("pid").is(id));
		// 执行查询，并找到一个匹配的条目
		Person person = mongoTemplate.findOne("employees", query,
				Person.class);

		return person;
	}

	/**
	 * 添加一个新的person
	 */
	public Boolean add(Person person) {

		try {

			// 设置一个新值的PID（UUID）
			person.setPid(UUID.randomUUID().toString());
			// 插入到DB
			mongoTemplate.insert("employees", person);

			return true;

		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 删除一个存在的person
	 */
	public Boolean delete(String id) {

		try {

			// 获取一个pid匹配id的条目
			Query query = new Query(Criteria.where("pid").is(id));
			// 运行查询和删除条目
			mongoTemplate.remove(query);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 编辑一个存在的Persn
	 */
	public Boolean edit(Person person) {

		try {

			// 获取一个pid匹配id的条目
			Query query = new Query(Criteria.where("pid").is(person.getPid()));

			// 声明一个更新的对象。
			// 此相匹配的更新修饰符在MongoDBThis matches the update modifiers available in
			// MongoDB
			Update update = new Update();

			update.set("firstName", person.getFirstName());
			mongoTemplate.updateMulti(query, update);

			update.set("lastName", person.getLastName());
			mongoTemplate.updateMulti(query, update);

			update.set("money", person.getMoney());
			mongoTemplate.updateMulti(query, update);

			return true;

		} catch (Exception e) {
			return false;
		}

	}

}
