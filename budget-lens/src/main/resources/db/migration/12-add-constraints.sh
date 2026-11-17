#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL

    ALTER TABLE registereduser
      ALTER email SET NOT NULL,
      ADD UNIQUE(email),
      ADD CHECK (email <> ''),
      ALTER password SET NOT NULL,
      ADD CHECK (password <> '');

    ALTER TABLE token
      ALTER token SET NOT NULL,
      ADD UNIQUE(token),
      ALTER user_id SET NOT NULL;

    ALTER TABLE frequency
      ALTER number SET NOT NULL,
      ADD UNIQUE(number),
      ADD CHECK (number > 0 AND number <= 365),
      ALTER description SET NOT NULL,
      ADD UNIQUE(description),
      ADD CHECK (description <> '');

    ALTER TABLE category
      ALTER name SET NOT NULL,
      ADD UNIQUE(name);

    ALTER TABLE expense
      ALTER cost SET NOT NULL,
      ADD CHECK (cost > 0),
      ALTER expense_date SET NOT NULL,
      ADD CHECK (expense_date < CURRENT_TIMESTAMP),
      ALTER category SET NOT NULL,
      ALTER source SET NOT NULL,
      ADD CHECK (source <> '');

    ALTER TABLE budget
      ALTER note SET NOT NULL,
      ADD CHECK (note <> ''),
      ALTER quota SET NOT NULL,
      ADD CHECK (quota >= 0),
      ALTER category SET NOT NULL,
      ALTER frequency SET NOT NULL;

    ALTER TABLE savingsgoal
      ALTER name SET NOT NULL,
      ADD UNIQUE(name),
      ADD CHECK (name <> ''),
      ALTER target_amount SET NOT NULL,
      ADD CHECK (target_amount >= 0),
      ALTER deadline SET NOT NULL,
      ADD CHECK (deadline > CURRENT_TIMESTAMP);

    ALTER TABLE insight
      ALTER year_month SET NOT NULL,
      ADD UNIQUE(year_month),
      ALTER median SET NOT NULL,
      ADD CHECK (median >= 0),
      ALTER percentile90 SET NOT NULL,
      ADD CHECK (percentile90 >= 0),
      ADD CHECK (standard_deviation_percent >= 0 AND standard_deviation_percent <= 1 ),
      ALTER category SET NOT NULL,
      ADD CHECK (expense_count >= 0),
      ALTER updated_at SET NOT NULL,
      ADD CHECK (safe_budget_confidence >= 0 AND safe_budget_confidence <= 1 ),
      ADD CHECK (budget_utilization >= 0 AND budget_utilization <= 1 );


    ALTER TABLE recordlimit
      ALTER class_name SET NOT NULL,
      ADD UNIQUE(class_name),
      ALTER current_count SET NOT NULL,
      ADD CHECK (current_count >= 0),
      ALTER record_limit SET NOT NULL,
      ADD CHECK (record_limit >= 0);

    ALTER TABLE source
      ALTER source SET NOT NULL,
      ADD CHECK (source <> ''),
      ALTER category SET NOT NULL,
      ADD CONSTRAINT source_source_category_key UNIQUE (source, category);


EOSQL