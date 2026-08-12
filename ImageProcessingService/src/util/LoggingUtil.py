import logging
import sys


class LoggingUtil:
    @staticmethod
    def get_logger(name):
        logger = logging.getLogger(name)
        logger.setLevel(logging.INFO)

        if not logger.handlers:
            handler = logging.StreamHandler(sys.stdout)
            formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')

            handler.setFormatter(formatter)
            handler.setLevel(logging.INFO)
            logger.addHandler(handler)

        return logger
