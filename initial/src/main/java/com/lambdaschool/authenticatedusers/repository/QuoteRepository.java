package com.lambdaschool.authenticatedusers.repository;

import com.lambdaschool.authenticatedusers.model.Quote;
import com.lambdaschool.authenticatedusers.view.CountQuotes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuoteRepository extends CrudRepository<Quote, Long>
{
                    //SQL
    @Query(value = "SELECT u.username as user, count(q.quotesid) as count FROM users u LEFT JOIN quotes q ON u.userid = q.userid GROUP BY username ORDER BY count desc ", nativeQuery = true) // the JPA runs the SQL by default but we want the database to run the SQl so we set nativeQuery to true

                      //banana name
    List<CountQuotes> getCountQuotes(); //Connecting to quotecounts controller
}
